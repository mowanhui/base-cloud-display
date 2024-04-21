package com.yamo.cdcommones.domain.bo;

import lombok.Data;

@Data
public class DeffaultToEsDataBO {
    String lonAttrName = "longitude";
    String latAttrName = "latitude";
    String locationName;
    String locateSplit;
    String datatype;
}
