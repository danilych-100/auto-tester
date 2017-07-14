package renue.fts.gateway.admin.autotest.helper;

import ru.kontur.fts.eps.schemas.common.ApplicationInfType;
import ru.kontur.fts.eps.schemas.common.BodyType;
import ru.kontur.fts.eps.schemas.common.HeaderType;
import ru.kontur.fts.eps.schemas.common.RoutingInfType;
import ru.kontur.fts.eps.schemas.common.SignatureType;
import ru.kontur.fts.eps.schemas.gwadmin.complextype.BaseDocType;
import ru.kontur.fts.eps.schemas.gwadmin.gwheader.GWHeaderType;
import ru.kontur.fts.eps.schemas.nci.ecopcatalog.EcopCatalogType;
import ru.kontur.fts.eps.schemas.nci.svhcatalog.SvhCatalogType;

/**
 *Dan.
 */
public final class EnvelopeHelper {

    private EnvelopeHelper(){}
    /**
     * @param header
     * @return
     */
    public static ApplicationInfType getApplicationInf(final HeaderType header) {
        for (Object obj : header.getAnyList()) {
            if (obj instanceof ApplicationInfType) {
                return (ApplicationInfType) obj;
            }
        }

        return null;
    }

    /**
     *
     * @param baseDoc
     * @return
     */
    public static EcopCatalogType getEcopCatalog(final BodyType baseDoc) {
        for (Object obj : baseDoc.getAnyList()) {
            if (obj instanceof EcopCatalogType) {
                return (EcopCatalogType) obj;
            }
        }

        return null;
    }

    /**
     *
     * @param baseDoc
     * @return
     */
    public static SvhCatalogType getSvhCatalogType(final BodyType baseDoc) {
        for (Object obj : baseDoc.getAnyList()) {
            if (obj instanceof SvhCatalogType) {
                return (SvhCatalogType) obj;
            }
        }

        return null;
    }

    /**
     *
     * @param header
     * @return
     */
    public static RoutingInfType getRoutingInf(final HeaderType header) {
        for (Object obj : header.getAnyList()) {
            if (obj instanceof RoutingInfType) {
                return (RoutingInfType) obj;
            }
        }

        return null;
    }

    /**
     *
     * @param header
     * @return
     */
    public static GWHeaderType getGWHeaderType(final HeaderType header) {
        for (Object obj : header.getAnyList()) {
            if (obj instanceof GWHeaderType) {
                return (GWHeaderType) obj;
            }
        }

        return null;
    }

    /**
     *
     * @param body
     * @return
     */
    public static BaseDocType getDocument(final BodyType body) {
        Object b = body.getAnyList().get(0);
        /*if (b instanceof BaseDocType) {
            return (BaseDocType) b;
        }*/

        if (b instanceof SignatureType) {
            SignatureType signature = (SignatureType) b;
            return (BaseDocType) signature.getObject().getAny();
        }

        return null;
    }
}
