package renue.fts.gateway.admin.autotest.documentutil;

import ru.kontur.fts.eps.schemas.gwadmin.blockparticipantnotification.BlockParticipantNotificationType;
import ru.kontur.fts.eps.schemas.gwadmin.blockparticipantrequest.BlockParticipantRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.changeprotocolrequest.ChangeProtocolRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;
import ru.kontur.fts.eps.schemas.gwadmin.createparticipantinfo.CreateParticipantInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.createparticipantnotification.CreateParticipantNotificationType;
import ru.kontur.fts.eps.schemas.gwadmin.createparticipantrequest.CreateParticipantRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.getorganizationrequest.GetOrganizationRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.getparticipantrequest.GetParticipantRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.modifyorganizationrequest.ModifyOrganizationRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.modifyparticipantnotification.ModifyParticipantNotificationType;
import ru.kontur.fts.eps.schemas.gwadmin.modifyparticipantrequest.ModifyParticipantRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.unblockparticipantnotification.UnblockParticipantNotificationType;
import ru.kontur.fts.eps.schemas.gwadmin.unblockparticipantrequest.UnblockParticipantRequestType;


/**
 * Define, what type of documentutil request is it by header.
 */
public class RequestDocumentHeaderChecker {
    private String requestHeader;

    public RequestDocumentHeaderChecker(final String requestHeader) {
        this.requestHeader = requestHeader;
    }

    /** Get RequsetDocumentType By Header.
     * @return type of documentutil.
     */
    public BaseDocType getDocumentTypeByHeader() {
        switch (requestHeader) {
            case "GW.BLOCK.REQ":
                return new BlockParticipantRequestType();

            case "GW.BLOCK.NOT":
                return new BlockParticipantNotificationType();

            case "GW.CHANGE_PROTOCOL.REQ":
                return new ChangeProtocolRequestType();

            case "GW.CREATE.REQ":
                return new CreateParticipantRequestType();

            case "GW.CREATE.ACC":
                return new CreateParticipantInfoType();

            case "GW.CREATE.NOT":
                return new CreateParticipantNotificationType();

            case "GW.GETORG.REQ":
                return new GetOrganizationRequestType();

            case "GW.GET_PARTICIPANT.REQ":
                return new GetParticipantRequestType();

            case "GW.MODIFY.REQ":
                return new ModifyOrganizationRequestType();

            case "GW.MODIFY.NOT":
                return new ModifyParticipantNotificationType();

            case "GW.MODIFY_PARTICIPANT.REQ":
                return new ModifyParticipantRequestType();

            case "GW.MODIFY_PARTICIPANT.NOT":
                return new ModifyParticipantNotificationType();

            case "GW.UNBLOCK.REQ":
                return new UnblockParticipantRequestType();

            case "GW.UNBLOCK.NOT":
                return new UnblockParticipantNotificationType();

            default:
                return new BaseDocType();
        }

    }

}
