package renue.fts.gateway.admin.autotest.scenarios;

import ru.kontur.fts.eps.schemas.common.RoutingInfType;

import java.util.Arrays;

/**
 * Created by Danil on 11.07.2017.
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
