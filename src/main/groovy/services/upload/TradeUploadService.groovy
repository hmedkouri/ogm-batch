package services.upload


interface TradeUploadService {

    List<String> fromExcel(InputStream fis)

}