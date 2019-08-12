/**
 * 
 */
package com.nyd.admin.service.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;


/**
 *
 */
public class BillWsmFieldSetMapper implements FieldSetMapper<ReconciliationWsm> {

	public ReconciliationWsm mapFieldSet(FieldSet fieldSet) throws BindException {
		ReconciliationWsm result = new ReconciliationWsm();
		result.setRemitStatus(fieldSet.readString("remitStatus"));
		result.setFundCode(fieldSet.readString("fundCode"));
		result.setContractStartTime(fieldSet.readString("contractStartTime"));
		result.setContractEndTime(fieldSet.readString("contractEndTime"));
		result.setOrderNo(fieldSet.readString("orderNo"));
		result.setReconciliationDay(fieldSet.readString("reconciliationDay"));
		result.setFlag(fieldSet.readString("flag"));
		result.setAmount(fieldSet.readString("amount"));

		return result;
	}
}
