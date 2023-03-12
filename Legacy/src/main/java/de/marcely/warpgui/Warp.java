package de.marcely.warpgui;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Deprecated
@Data
public class Warp implements Serializable {
    private static final long serialVersionUID = 100042053024876811L;

    private String name;
    private String iconName;
    private short iconID;
    private String prefix = "";
    private List<String> lores = new ArrayList<>();
}