package tm.wearable.wearabletfm.data.model

class FitbitOauth(var codeVerifier: String,var codeChallenge: String,var url: String) {
    constructor(): this("","","")
}