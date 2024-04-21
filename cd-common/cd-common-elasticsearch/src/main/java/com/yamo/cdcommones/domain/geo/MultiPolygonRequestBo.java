package com.yamo.cdcommones.domain.geo;

import lombok.Data;

@Data
public class MultiPolygonRequestBo extends GeoRequestBO{
    private String coordinates;
}
