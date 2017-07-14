package renue.fts.gateway.admin.autotest.documentutil;


import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;
import ru.kontur.fts.eps.schemas.gwadmin.result.ResultType;

/**
 * Created by Danil on 13.07.2017.
 */
public class ResponseDocumentHeaderChecker {

    private String responseHeader;

    public ResponseDocumentHeaderChecker(final String responseHeader) {
        this.responseHeader = responseHeader;
    }

    /**
     * @return
     */
    public BaseDocType getResponseDocType() {
        switch (responseHeader) {
            case "GW.ERROR":
                return new ResultType();

            default:
                return new BaseDocType();
        }
    }
}
