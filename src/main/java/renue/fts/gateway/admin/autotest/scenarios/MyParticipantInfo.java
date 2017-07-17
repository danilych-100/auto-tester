package renue.fts.gateway.admin.autotest.scenarios;

import org.joda.time.LocalDate;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.ParticipantInfoType;


/**
 * Class wrapper over ParticipantInfoType.
 */
public class MyParticipantInfo extends ParticipantInfoType {

    public String getDocBasisDateString() {
        return getDocBasisDate().toString("yyyy-MM-dd");
    }

    /**
     * Setter.
     * @param docBasisDateString
     */
    public void setDocBasisDateString(final String docBasisDateString) {
        setDocBasisDate(LocalDate.parse(docBasisDateString));
    }
}
