package com.icthh.xm.lep.api;

/**
 * The {@link Version} interface.
 */
public interface Version {

    /**
     * Gets version identificator, for example:
     * <br>
     * 'b324', '1-alpha', '1.2.3', '1.0-RELEASE', '2.2-RC1', '2.2.3-M1', '12032017112331', etc.
     *
     * @return version id
     */
    String getId();

}
