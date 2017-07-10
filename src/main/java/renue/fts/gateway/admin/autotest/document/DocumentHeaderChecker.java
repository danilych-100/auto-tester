package renue.fts.gateway.admin.autotest.document;

import ru.kontur.fts.eps.schemas.gwadmin.blockparticipantnotification.BlockParticipantNotificationType;
import ru.kontur.fts.eps.schemas.gwadmin.blockparticipantrequest.BlockParticipantRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.changeprotocolrequest.ChangeProtocolRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;
import ru.kontur.fts.eps.schemas.gwadmin.createparticipantrequest.CreateParticipantRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.result.ResultType;

/**
 * Created by Danil on 10.07.2017.
 */
public class DocumentHeaderChecker {
    private String requestHeader;

    public DocumentHeaderChecker(final String requestHeader) {
        this.requestHeader = requestHeader;
    }

    /**Get.
     * @return type of document.
     */
    public BaseDocType getDocumentTypeByHeader() {
        switch (requestHeader) {
            case "GW.BLOCK.REQ":
                return new BlockParticipantRequestType();

            case "GW.BLOCK.RES":
                return new ResultType();

            case "GW.BLOCK.NOT":
                return new BlockParticipantNotificationType();

            case "GW.CHANGE_PROTOCOL.REQ":
                return new ChangeProtocolRequestType();

            case "GW.CREATE.REQ":
                return new CreateParticipantRequestType();

            case "GW.CHANGE_PROTOCOL.RES":
                return new ResultType();

            case "GW.CREATE.RES":
                return new ResultType();

            default:
                return new BaseDocType();
        }

    }

}
