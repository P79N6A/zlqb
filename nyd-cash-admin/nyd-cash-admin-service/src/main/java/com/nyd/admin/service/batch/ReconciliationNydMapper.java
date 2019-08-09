package com.nyd.admin.service.batch;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Cong Yuxiang
 * 2017/11/27
 **/
public class ReconciliationNydMapper  implements RowMapper<ReconciliationWsm> {
    @Nullable
    @Override
    public ReconciliationWsm mapRow(ResultSet resultSet, int i) throws SQLException {
        ReconciliationWsm reconciliationWsm = new ReconciliationWsm();
        reconciliationWsm.setOrderNo(resultSet.getString("order_no"));
        reconciliationWsm.setFundCode(resultSet.getString("fund_code"));
        reconciliationWsm.setRemitStatus(resultSet.getString("remit_status"));
        reconciliationWsm.setFlag(resultSet.getString("flag"));

        reconciliationWsm.setReconciliationDay("");
        reconciliationWsm.setAmount(String.valueOf(resultSet.getDouble("remit_amount")));
        reconciliationWsm.setContractStartTime("");
        reconciliationWsm.setContractEndTime("");
        return reconciliationWsm;
    }
}
