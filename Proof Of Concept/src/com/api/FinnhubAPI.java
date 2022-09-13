package com.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

// TODO: Retired api, not in use cause slow and inconsistent
public class FinnhubAPI {
    RequestHandler ReqHandler = new RequestHandler();
    private String api_key_token = "c9tbouqad3i1pjtuimu0";

    public FinnhubAPI(){
    }
    
    // TODO: make this function cache requests... so that probably at startup, all companies in watchlists will be called so it's faster for later... 
    public JsonArray make_request(String request_url, boolean cache) throws Exception {
        return ReqHandler.get(request_url+"&token="+api_key_token, cache);
    }

    public JsonArray company_profile(String ticker) throws Exception {
        String request_url = "https://finnhub.io/api/v1/stock/profile2?symbol="+ticker;
        return make_request(request_url, true);
    }

    public static String[] constituents(String ticker) {
        if (ticker.equals("NDX")){
            return new String[]{"CTSH","WDAY","AZN","COST","FISV","KHC","PCAR","MTCH","DXCM","TXN","ATVI","ZS","ORLY","DLTR","VRSN","ASML","KDP","SIRI","SPLK","CTAS","REGN","PEP","PAYX","ANSS","AMD","IDXX","ISRG","ALGN","CSX","DOCU","CSCO","PDD","FB","ADBE","GILD","CMCSA","MSFT","CHTR","XEL","SWKS","VRSK","AAPL","MNST","EBAY","ODFL","CEG","NVDA","AVGO","OKTA","LCID","BKNG","ADSK","ILMN","INTU","ROST","NXPI","PYPL","ADP","ADI","MAR","KLAC","NTES","ZM","ABNB","MU","JD","BIIB","MCHP","EXC","EA","AMZN","GOOG","CRWD","AMAT","TSLA","AEP","LRCX","GOOGL","WBA","SGEN","SBUX","QCOM","MDLZ","NFLX","AMGN","VRTX","FAST","BIDU","MRNA","TEAM","DDOG","PANW","INTC","SNPS","CPRT","HON","MELI","CDNS","TMUS","FTNT","MRVL","LULU"};
        }
        if (ticker.equals("SPY")){
            return new String[]{"RCL","MDT","AKAM","FAST","DG","PSA","IRM","HAS","JNJ","PNR","CRL","SBNY","SHW","MNST","OGN","MAR","RHI","KIM","STX","ESS","CB","ROST","SPGI","COF","SIVB","SRE","TSLA","EPAM","EIX","LYB","IR","EQR","WYNN","CI","SYK","NDSN","TFC","CBOE","GIS","CRM","APTV","ULTA","NI","LOW","PFE","AMGN","MET","AES","EXR","DISH","LVS","DXCM","TXN","NKE","TDY","UHS","NTRS","SNPS","VTRS","PPL","FIS","APH","QRVO","NOW","DRI","ANTM","MRO","MSFT","AXP","FOXA","FDS","AON","TMO","AIZ","MCHP","DLR","FTV","EMR","JPM","HSIC","TECH","BLL","WM","RF","F","HPQ","PAYX","COST","CFG","CHTR","MGM","AIG","FDX","EW","ABC","ROP","POOL","CAG","ZBH","DIS","SCHW","TMUS","BMY","LLY","MSI","MTB","PNW","CPB","AMT","BRO","NTAP","MKC","CMS","HSY","MOH","WY","CTSH","MMC","WHR","UAL","NVDA","BRK.B","SBAC","MCO","ALGN","CHRW","MA","BKR","GL","IVZ","BKNG","NCLH","FRC","LUMN","AEP","SNA","WFC","MAS","AVGO","HII","FRT","CE","AMD","LIN","TTWO","A","GNRC","SO","PCAR","CME","CMI","AVB","HLT","NFLX","MO","GWW","MTD","ADBE","BAC","MDLZ","TPR","MKTX","ON","VLO","EFX","BAX","TWTR","PNC","ABBV","WRK","CINF","COO","CZR","GOOGL","PM","LNC","GRMN","AZO","DXC","CAH","BLK","HRL","SWK","NWL","WBA","VICI","ARE","C","FCX","LNT","TYL","KR","NSC","LRCX","PVH","CTAS","TFX","PTC","CVS","PEAK","ZTS","REG","SEDG","IP","FITB","LEN","CHD","WAT","O","RTX","VZ","DOW","JNPR","HAL","HWM","CDNS","MMM","IFF","GPN","CMG","ALK","UPS","CTXS","WDC","CTRA","KDP","AOS","CCL","PLD","NDAQ","PARA","ETSY","CLX","J","MHK","TDG","RSG","APA","DHR","DAL","XEL","FLT","FFIV","HPE","BXP","HUM","ACN","DLTR","KHC","IPG","LMT","JKHY","XOM","FE","TSN","AMP","MCK","KEYS","ECL","CF","V","BA","UNH","VMC","TEL","PEG","HCA","ORCL","EA","STZ","FOX","WELL","ETN","MRK","VFC","NLSN","PG","TSCO","RE","T","NLOK","LDOS","ABMD","KLAC","IDXX","AAP","BDX","AWK","GOOG","HES","CDAY","NUE","AVY","BWA","INTU","EMN","AAL","PENN","ISRG","VTR","GE","L","TER","EL","BBWI","UDR","ANET","FANG","ABT","DFS","STT","EXPE","SBUX","SWKS","PSX","FTNT","AFL","BIIB","ALL","DD","IBM","CMA","EXPD","ATVI","CTVA","STE","HBAN","DVA","MPC","URI","BBY","AMCR","LYV","FMC","KMB","WMB","VRSK","LW","CPT","GM","EOG","CTLT","MAA","PAYC","CL","QCOM","WRB","CVX","GD","PYPL","WAB","TRMB","KEY","NEM","BK","EXC","ES","MLM","ROL","HD","FISV","GILD","ICE","META","ADM","BR","OMC","HOLX","PWR","XYL","ALLE","TXT","RL","SJM","INCY","INTC","MU","PH","WMT","DHI","CARR","DPZ","BF.B","GLW","ALB","BSX","NVR","ZION","AEE","PFG","DVN","OXY","LHX","PXD","AMZN","K","NEE","RMD","PHM","ED","JBHT","MOS","ILMN","OTIS","ADP","PRU","AAPL","NWSA","LKQ","ATO","SEE","ORLY","CPRT","GPC","ITW","ADI","NRG","HON","LUV","TJX","SLB","SPG","D","DRE","KMI","TROW","NOC","SYY","ETR","REGN","VRTX","ZBRA","ADSK","UNP","CCI","ENPH","PGR","DOV","TGT","EQIX","ANSS","MS","VRSN","IQV","DGX","PKI","COP","IEX","CDW","EBAY","HST","VNO","AMAT","BIO","TAP","XRAY","CMCSA","OKE","AME","RJF","BEN","PKG","CSCO","PPG","FBHS","IT","CAT","AJG","MTCH","NXPI","JCI","CNC","WEC","MRNA","NWS","DE","ROK","CSX","DTE","MSCI","WST","HIG","APD","TRV","MPWR","USB","CBRE","KMX","CEG","EVRG","CNP","KO","LH","DISCA","ODFL","MCD","TT","PEP","YUM","DUK","WTW","GS","SYF"};
        }
        return null;
    }

}
