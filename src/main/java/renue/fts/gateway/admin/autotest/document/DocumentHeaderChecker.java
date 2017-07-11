package renue.fts.gateway.admin.autotest.document;

import ru.kontur.fts.eps.schemas.gwadmin.blockparticipantnotification.BlockParticipantNotificationType;
import ru.kontur.fts.eps.schemas.gwadmin.blockparticipantrequest.BlockParticipantRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.changeprotocolrequest.ChangeProtocolRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.commentedresponse.CommentedResponseType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;
import ru.kontur.fts.eps.schemas.gwadmin.createparticipantinfo.CreateParticipantInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.createparticipantnotification.CreateParticipantNotificationType;
import ru.kontur.fts.eps.schemas.gwadmin.createparticipantrequest.CreateParticipantRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.getorganizationrequest.GetOrganizationRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.getorganizationresponse.GetOrganizationResponseType;
import ru.kontur.fts.eps.schemas.gwadmin.getparticipantrequest.GetParticipantRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.getparticipantresponse.GetParticipantResponseType;
import ru.kontur.fts.eps.schemas.gwadmin.modifyorganizationrequest.ModifyOrganizationRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.modifyparticipantnotification.ModifyParticipantNotificationType;
import ru.kontur.fts.eps.schemas.gwadmin.modifyparticipantrequest.ModifyParticipantRequestType;
import ru.kontur.fts.eps.schemas.gwadmin.result.ResultType;
import ru.kontur.fts.eps.schemas.gwadmin.unblockparticipantnotification.UnblockParticipantNotificationType;
import ru.kontur.fts.eps.schemas.gwadmin.unblockparticipantrequest.UnblockParticipantRequestType;

//CHECKSTYLE:OFF

/**
 * Created by Danil on 10.07.2017.
 */
public class DocumentHeaderChecker {
    private String requestHeader;

    public DocumentHeaderChecker(final String requestHeader) {
        this.requestHeader = requestHeader;
    }

    /** Get DocumentType By Header.
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

            case "GW.CREATE.ACC":
                return new CreateParticipantInfoType();

            case "GW.CREATE.DEC":
                return new CommentedResponseType();

            case "GW.ERROR":
                return new ResultType();

            case "GW.CREATE.NOT":
                return new CreateParticipantNotificationType();

            case "GW.GETORG.REQ":
                return new GetOrganizationRequestType();

            case "GW.GETORG.RES":
                return new GetOrganizationResponseType();

            case "GW.GET_PARTICIPANT.REQ":
                return new GetParticipantRequestType();

            case "GW.GET_PARTICIPANT.RES":
                return new GetParticipantResponseType();

            case "GW.MODIFY.REQ":
                return new ModifyOrganizationRequestType();

            case "GW.MODIFY.RES":
                return new ResultType();

            case "GW.MODIFY.ACC":
                return new ResultType();

            case "GW.MODIFY.DEC":
                return new CommentedResponseType();

            case "GW.MODIFY.NOT":
                return new ModifyParticipantNotificationType();

            case "GW.MODIFY_PARTICIPANT.REQ":
                return new ModifyParticipantRequestType();

            case "GW.MODIFY_PARTICIPANT.RES":
                return new ResultType();

            case "GW.MODIFY_PARTICIPANT.ACC":
                return new ResultType();

            case "GW.MODIFY_PARTICIPANT.DEC":
                return new CommentedResponseType();

            case "GW.MODIFY_PARTICIPANT.NOT":
                return new ModifyParticipantNotificationType();

            case "GW.UNBLOCK.REQ":
                return new UnblockParticipantRequestType();

            case "GW.UNBLOCK.RES":
                return new ResultType();

            case "GW.UNBLOCK.NOT":
                return new UnblockParticipantNotificationType();

            default:
                return new BaseDocType();
        }

    }

}
