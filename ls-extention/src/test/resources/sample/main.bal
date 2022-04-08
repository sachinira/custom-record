import ballerina/os;
import ballerina/io;
import ballerinax/salesforce.rest;

configurable string & readonly clientId = os:getEnv("CLIENT_ID");
configurable string & readonly clientSecret = os:getEnv("CLIENT_SECRET");
configurable string & readonly refreshToken = os:getEnv("REFRESH_TOKEN");
configurable string & readonly refreshUrl = os:getEnv("REFRESH_URL");
configurable string & readonly baseUrl = os:getEnv("EP_URL");

rest:ConnectionConfig sfConfig = {
    baseUrl: baseUrl,
    clientConfig: {
        clientId: clientId,
        clientSecret: clientSecret,
        refreshToken: refreshToken,
        refreshUrl: refreshUrl
    }
};

rest:Client baseClient = check new (sfConfig);

public function main() returns error? {

    string path = "/services/data/v48.0/sobjects/Account/0015g00000XOzNrAAL";
    record {}|rest:Error response = baseClient->getRecord(path);
    io:println(response);
}
