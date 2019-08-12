package com.nyd.capital.model.qcgz;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author liuqiu
 */
@Data
public class LoanFailListRequest implements Serializable {
    private List<String> assetIds;
}
