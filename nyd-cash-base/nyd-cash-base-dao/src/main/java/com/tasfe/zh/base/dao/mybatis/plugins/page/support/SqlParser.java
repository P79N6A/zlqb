package com.tasfe.zh.base.dao.mybatis.plugins.page.support;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * sql解析类，提供更智能的count查询sql
 */
@SuppressWarnings("rawtypes")
public class SqlParser {
    private static final List<SelectItem> COUNT_ITEM;
    private static final Alias TABLE_ALIAS;

    static {
        COUNT_ITEM = new ArrayList<SelectItem>();
        COUNT_ITEM.add(new SelectExpressionItem(new Column("count(*)")));

        TABLE_ALIAS = new Alias("table_count");
        TABLE_ALIAS.setUseAs(false);
    }

    //缓存已经修改过的sql
    private Map<String, String> CACHE = new ConcurrentHashMap<String, String>();

    public void isSupportedSql(String sql) {
        if (sql.trim().toUpperCase().endsWith("FOR UPDATE")) {
            throw new RuntimeException("分页插件不支持包含for update的sql");
        }
    }

    /**
     * 获取智能的countSql
     *
     * @param sql
     * @return
     */
    public String getSmartCountSql(String sql) {
        //校验是否支持该sql
        isSupportedSql(sql);
        if (CACHE.get(sql) != null) {
            return CACHE.get(sql);
        }
        //解析SQL
        Statement stmt = null;
        try {
            stmt = CCJSqlParserUtil.parse(sql);
        } catch (JSQLParserException e) {
            //无法解析的用一般方法返回count语句
            String countSql = getSimpleCountSql(sql);
            CACHE.put(sql, countSql);
            return countSql;
        }
        Select select = (Select) stmt;
        SelectBody selectBody = select.getSelectBody();
        //处理body
        processSelectBody(selectBody);
        //处理with
        processWithItemsList(select.getWithItemsList());
        //处理为count查询
        sqlToCount(select);
        String result = select.toString();
        CACHE.put(sql, result);
        return result;
    }

    /**
     * 获取普通的Count-sql
     *
     * @param sql 原查询sql
     * @return 返回count查询sql
     */
    public String getSimpleCountSql(final String sql) {
        isSupportedSql(sql);
        StringBuilder stringBuilder = new StringBuilder(sql.length() + 40);
        stringBuilder.append("select count(*) from (");
        stringBuilder.append(sql);
        stringBuilder.append(") tmp_count");
        return stringBuilder.toString();
    }

    /**
     * 将sql转换为count查询
     *
     * @param select
     */
    public void sqlToCount(Select select) {
        SelectBody selectBody = select.getSelectBody();
        // select中包含参数时在else中处理
        // select中包含group by时在else中处理
        if (selectBody instanceof PlainSelect
                && !selectItemsHashParameters(((PlainSelect) selectBody).getSelectItems())
                && ((PlainSelect) selectBody).getGroupByColumnReferences() == null
                && ((PlainSelect) selectBody).getDistinct() == null) {
            ((PlainSelect) selectBody).setSelectItems(COUNT_ITEM);
        } else {
            PlainSelect plainSelect = new PlainSelect();
            SubSelect subSelect = new SubSelect();
            subSelect.setSelectBody(selectBody);
            subSelect.setAlias(TABLE_ALIAS);
            plainSelect.setFromItem(subSelect);
            plainSelect.setSelectItems(COUNT_ITEM);
            select.setSelectBody(plainSelect);
        }
    }

    /**
     * 处理selectBody去除Order by
     *
     * @param selectBody
     */
    public void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() != null) {
                processSelectBody(withItem.getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            if (operationList.getPlainSelects() != null && operationList.getPlainSelects().size() > 0) {
                List<PlainSelect> plainSelects = operationList.getPlainSelects();
                for (PlainSelect plainSelect : plainSelects) {
                    processPlainSelect(plainSelect);
                }
            }
            if (!orderByHashParameters(operationList.getOrderByElements())) {
                operationList.setOrderByElements(null);
            }
        }
    }

    /**
     * 处理PlainSelect类型的selectBody
     *
     * @param plainSelect
     */
    public void processPlainSelect(PlainSelect plainSelect) {
        if (!orderByHashParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements(null);
        }
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem());
        }
        if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join : joins) {
                if (join.getRightItem() != null) {
                    processFromItem(join.getRightItem());
                }
            }
        }
    }

    /**
     * 处理WithItem
     *
     * @param withItemsList
     */
    public void processWithItemsList(List<WithItem> withItemsList) {
        if (withItemsList != null && withItemsList.size() > 0) {
            for (WithItem item : withItemsList) {
                processSelectBody(item.getSelectBody());
            }
        }
    }

    /**
     * 处理子查询
     *
     * @param fromItem
     */
    public void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoin() != null) {
                if (subJoin.getJoin().getRightItem() != null) {
                    processFromItem(subJoin.getJoin().getRightItem());
                }
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {

        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
        //Table时不用处理
    }

    /**
     * 判断Orderby是否包含参数，有参数的不能去
     *
     * @param orderByElements
     * @return
     */
    public boolean orderByHashParameters(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return false;
        }
        for (OrderByElement orderByElement : orderByElements) {
            if (orderByElement.toString().contains("?")) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断selectItems是否包含参数，有参数的不能去
     *
     * @param selectItems
     * @return
     */
    public boolean selectItemsHashParameters(List<SelectItem> selectItems) {
        if (selectItems == null) {
            return false;
        }
        for (SelectItem selectItem : selectItems) {
            if (selectItem.toString().contains("?")) {
                return true;
            }
        }
        return false;
    }
}
