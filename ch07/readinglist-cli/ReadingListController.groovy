import org.springframework.boot.actuate.metrics.CounterService
import org.springframework.boot.actuate.metrics.GaugeService

@Controller
@RequestMapping("/")
class ReadingListController {

    String reader = "craig"

    @Autowired
    ReadingListRepository readingListRepository
    
    @Autowired
    CounterService counterService
    
    @Autowired
    GaugeService gaugeService

    @RequestMapping(method = RequestMethod.GET)
    def readersBooks(Model model) {
        List<Book> readingList = readingListRepository.findByReader(reader)
        if (readingList) {
            model.addAttribute("books", readingList)
        }
        "readingList"
    }
  
    @RequestMapping(method = RequestMethod.POST)
    def addToReadingList(Book book) {
        book.setReader(reader)
        readingListRepository.save(book)
        counterService.increment("books.saved")
        gaugeService.submit("books.last.saved", System.currentTimeMillis())
        "redirect:/"
    }

}
