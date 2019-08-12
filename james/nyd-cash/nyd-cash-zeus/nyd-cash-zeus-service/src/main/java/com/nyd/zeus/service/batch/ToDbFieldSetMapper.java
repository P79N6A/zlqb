/**
 * 
 */
package com.nyd.zeus.service.batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;


/**
 *
 */
public class ToDbFieldSetMapper implements FieldSetMapper<ReconciliationWsmPo> {

	public ReconciliationWsmPo mapFieldSet(FieldSet fieldSet) throws BindException {
		ReconciliationWsmPo result = new ReconciliationWsmPo();
		result.setOrderNo(fieldSet.readString("orderNo"));
		result.setAmount(fieldSet.readDouble("amount"));
		result.setContractStartTime(fieldSet.readDate("contractStartTime"));
		result.setContractEndTime(fieldSet.readDate("contractEndTime"));
		result.setReconciliationDay(fieldSet.readDate("reconciliationDay"));
		result.setFundCode(fieldSet.readString("fundCode"));
		result.setRemitStatus(fieldSet.readString("remitStatus"));
		result.setResultCode(fieldSet.readString("resultCode"));
		result.setAmountOwn(fieldSet.readDouble("amountOwn"));
		return result;
	}
}
