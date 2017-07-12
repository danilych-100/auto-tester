package renue.fts.gateway.admin.autotest.scenarios;

import lombok.Data;
import ru.kontur.fts.eps.schemas.gwadmin.gwheader.GWHeaderType;

/**
 * Transaction Headers.
 */
@Data
public class Header {
    private MyRoutingInfo routingInf;
    private GWHeaderType gwHeader;
}
