package renue.fts.gateway.admin.autotest.config;

import lombok.Data;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.OrganizationInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.ParticipantInfoType;
import ru.kontur.fts.eps.schemas.gwadmin.simpletype.ParticipantMode;

/**
 * Created by Danil on 07.07.2017.
 */
@Data
public class Request {
    private Header header;
    private BaseDocType baseDocType;
    private OrganizationInfoType organizationInfoType;
    private ParticipantInfoType participantInfoType;
    private ParticipantMode participantMode;


}
