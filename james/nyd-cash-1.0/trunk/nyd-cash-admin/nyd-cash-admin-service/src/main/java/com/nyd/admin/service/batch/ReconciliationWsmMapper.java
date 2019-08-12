package com.nyd.admin.service.batch;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Cong Yuxiang
 * 2017/11/25
 **/
public class ReconciliationWsmMapper implements RowMapper<ReconciliationWsm>{
    @Nullable
    @Override
    public ReconciliationWsm mapRow(ResultSet resultSet, int i) throws SQLException {
        ReconciliationWsm reconciliationWsm = new ReconciliationWsm();
        reconciliationWsm.setOrderNo(resultSet.getString("merchant_order_no"));
        reconciliationWsm.setAmount(String.valueOf(resultSet.getDouble("amount")));
        reconciliationWsm.setContractStartTime(resultSet.getString("contract_start_time"));
        reconciliationWsm.setContractEndTime(resultSet.getString("contract_end_time"));
        reconciliationWsm.setFlag(resultSet.getString("flag"));
        reconciliationWsm.setReconciliationDay(resultSet.getString("reconciliation_day"));

        reconciliationWsm.setFundCode("");
        reconciliationWsm.setRemitStatus("");
        return reconciliationWsm;
    }
}
