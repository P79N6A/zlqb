package com.nyd.order.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Gem {

        /** 资金方名称 */
        private String name;
        /** 流量比例 */
        private int priority;



}
