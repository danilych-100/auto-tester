package renue.fts.gateway.admin.autotest.scenarios;

import ru.kontur.fts.eps.schemas.common.RoutingInfType;

import java.util.Arrays;

/**
 * Class wrapper over RoutingInfoType.
 */
public class MyRoutingInfo extends RoutingInfType {

    public String getReceiverInformationString(){
        return getReceiverInformationList().get(0);
    }

    /**
     * Set stringReceiverInf.
     * @param receiverInformationString
     */
    public void setReceiverInformationString(final String receiverInformationString){
        setReceiverInformationList(Arrays.asList(receiverInformationString));
    }
}
