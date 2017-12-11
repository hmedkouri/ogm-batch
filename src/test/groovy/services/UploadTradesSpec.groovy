package services

import com.google.inject.Inject
import modules.InProcessNeo4jServerModule
import modules.MappingModule
import modules.RepositoryModule
import org.junit.Rule
import services.upload.TradeUploadService
import services.upload.UploadModule
import spock.guice.UseModules
import spock.lang.Specification
import spock.lang.Subject
import util.ResourceFile

@UseModules([
        InProcessNeo4jServerModule,
        RepositoryModule,
        MappingModule,
        UploadModule
])
class UploadTradesSpec extends Specification {

    @Rule
    public ResourceFile all = new ResourceFile("/excel/TradePortfolio.xlsx")

    @Subject
    @Inject
    TradeUploadService uploadService

    void "load a single bilateral trade"() {
        when:
        def ids = uploadService.fromExcel(all.createInputStream())

        then:
        with(ids){
            !isEmpty()
            size() == 1200
        }

    }
}
