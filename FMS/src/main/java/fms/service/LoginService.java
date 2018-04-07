package fms.service;

import java.util.logging.Level;

import fms.dao.AuthTokensDao;
import fms.dao.UsersDao;
import fmshared.fmrequest.LoginRequest;
import fmshared.fmresult.LoginResult;
import fmshared.model.AuthTokens;


/** Service for login calls from the API.
 *
 * @author TylorBayer
 */
public class LoginService extends FMSService {

    /** Logs in the user and returns an auth token.
     *
     * @param logReq LoginRequest object
     * @return LoginResult object
     */
    public static LoginResult login(LoginRequest logReq) {
        try {
            String[] user = UsersDao.verify(logReq.getUserName(), logReq.getPassword());

            AuthTokens[] authToken = new AuthTokens[1];
            authToken[0] = new AuthTokens(logReq.getUserName());
            AuthTokensDao.insert(authToken);

            return new LoginResult(authToken[0].getValues().get(0), logReq.getUserName(), user[0], user[1], user[2]);
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new LoginResult(e.getMessage());
        }
    }
}
