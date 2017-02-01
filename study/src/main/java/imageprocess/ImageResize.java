/**
 * @author 오연지
 */
@Service
public class CineplexApiServiceImpl implements CineplexApiService {
    @Autowired
    private CineplexCrawlingApiService cineplexCrawlingApiService;
    @Autowired
    private CineplexDataApiService cineplexDataApiService;
    @Autowired
    private MovieDAO movieDAO;
    @Autowired
    private CinemaDAO cinemaDAO;
    @Autowired
    private MovieShowtimesDAO movieShowtimesDAO;
    @Autowired
    private CineplexPaymentService cineplexPaymentService;
    @Autowired
    private CineplexPaymentDAO cineplexPaymentDAO;
    @Autowired
    private CineplexPaymentWaitService cineplexPaymentWaitService;
    @Autowired
    private CineplexMyTicketDAO cineplexMyTicketDAO;
    @Autowired
    private CineplexShareService cineplexShareService;

    @Value("${cineplex.api.domain}")
    private String cineplexUrl;
    @Value("${cineplex.file.path}")
    private String cineplexFilePath;

    private static final Log log = LogFactory.getLog(CineplexApiServiceImpl.class);

    @Override
    public void moviePosterResize() throws IOException {
        List<Map<String, String>> posterList = movieDAO.selectNewMoviePosterPathList(ApiDataProcessStatus.NEW.getStatus());
        // file path
        String today = LPDateUtil.getTodate(LPDateUtil.DATE_FORMAT_YMD);
        String filePath = cineplexFilePath + today + "/thumb/";

        File dir = new File(filePath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (Map<String, String> poster : posterList) {
            String lpPosterOrigin = poster.get("lpPosterOrigin");
            String lpPosterThumb = filePath + poster.get("fileName");
            String extension = poster.get("extension");
            String movieId = poster.get("movieId");
            ImageUtil.lpMoviePosterOriginResize(lpPosterOrigin, lpPosterThumb, extension);

            movieDAO.updateLpPosterThumbPath(movieId, lpPosterThumb);
        }
    }

    @Override
    @Transactional
    public boolean updateMovieStatusToLive() {
        updateMovieServiceStatus(ApiDataProcessStatus.LIVE.getStatus(), ApiDataProcessStatus.OLD.getStatus());
        updateMovieServiceStatus(ApiDataProcessStatus.NEW.getStatus(), ApiDataProcessStatus.LIVE.getStatus());
        return true;
    }

    private void updateMovieServiceStatus(String from, String to) {
        movieDAO.updateMovieStatus(from, to);
    }
  
}
