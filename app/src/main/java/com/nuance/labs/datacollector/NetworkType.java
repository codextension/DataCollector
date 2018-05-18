package com.nuance.labs.datacollector;

public enum NetworkType {
    /** Network type is unknown */
    NETWORK_TYPE_UNKNOWN(0),
    /** Current network is GPRS */
    NETWORK_TYPE_GPRS(1),
    NETWORK_TYPE_EDGE(2),
    /** Current network is UMTS */
    NETWORK_TYPE_UMTS(3),
    /** Current network is CDMA: Either IS95A or IS95B*/
    NETWORK_TYPE_CDMA(4),
    /** Current network is EVDO revision 0*/
    NETWORK_TYPE_EVDO_0(5),
    /** Current network is EVDO revision A*/
    NETWORK_TYPE_EVDO_A(6),
    /** Current network is 1xRTT*/
    NETWORK_TYPE_1xRTT(7),
    /** Current network is HSDPA */
    NETWORK_TYPE_HSDPA(8),
    /** Current network is HSUPA */
    NETWORK_TYPE_HSUPA(9),
    /** Current network is HSPA */
    NETWORK_TYPE_HSPA(10),
    /** Current network is iDen */
    NETWORK_TYPE_IDEN(11),
    /** Current network is EVDO revision B*/
    NETWORK_TYPE_EVDO_B(12),
    /** Current network is LTE */
    NETWORK_TYPE_LTE(13),
    /** Current network is eHRPD */
    NETWORK_TYPE_EHRPD(14),
    /** Current network is HSPA+ */
    NETWORK_TYPE_HSPAP(15),
    /** Current network is GSM */
    NETWORK_TYPE_GSM(16),
    /** Current network is TD_SCDMA */
    NETWORK_TYPE_TD_SCDMA(17),
    /** Current network is IWLAN */
    NETWORK_TYPE_IWLAN(18),
    /** Current network is LTE_CA {@hide} */
    NETWORK_TYPE_LTE_CA(19);

    private int id;
    NetworkType(int i) {
    this.id=i;
    }

    public static NetworkType fromInt(int v) {
        for (NetworkType b : NetworkType.values()) {
            if (b.id == v) {
                return b;
            }
        }
        return null;
    }
}
