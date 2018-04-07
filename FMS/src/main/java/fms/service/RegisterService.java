package fms.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import fms.dao.AuthTokensDao;
import fms.dao.UsersDao;
import fmshared.fmrequest.RegisterRequest;
import fmshared.fmresult.ClearResult;
import fmshared.fmresult.RegisterResult;
import fmshared.model.AuthTokens;
import fmshared.model.Users;


/** Service for register calls from the API.
 *
 * @author TylorBayer
 */
public class RegisterService extends FMSService {

    /** Creates a new user account, generates 4 generations of ancestor data for the new user, logs the user in, and returns an auth token.
     *
     * @param regReq RegisterRequest object
     * @return RegisterResult object
     */
    public static RegisterResult register(RegisterRequest regReq) {
        try {
            Users[] user = new Users[1];
            user[0] = new Users(regReq);
            UsersDao.insert(user);

            AuthTokens[] authToken = new AuthTokens[1];
            authToken[0] = new AuthTokens(regReq.getUserName());
            AuthTokensDao.insert(authToken);

            FillService.fill(regReq.getUserName());

            return new RegisterResult(authToken[0].getValues().get(0), regReq.getUserName(), user[0].getValues().get(6));
        }
        catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new RegisterResult(e.getMessage());
        }
    }
}
