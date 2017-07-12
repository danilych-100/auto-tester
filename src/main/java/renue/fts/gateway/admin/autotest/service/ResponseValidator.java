package renue.fts.gateway.admin.autotest.service;


import org.springframework.stereotype.Service;
import renue.fts.gateway.admin.autotest.scenarios.Response;
import renue.fts.gateway.admin.autotest.validation.ValidationResult;
import ru.kontur.fts.eps.schemas.common.EnvelopeType;

/**
 * Created by Danil on 12.07.2017.
 */
@Service
public class ResponseValidator {
    /**
     * validate response.
     * @param response
     * @param envelopeType
     * @return
     */
    public ValidationResult validate(final Response response,final EnvelopeType envelopeType) {
        return null;
    }
}
