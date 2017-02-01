
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
    @Transactional
    public boolean processMoviesNowShowing() {
        movieDAO.deleteMovieList(MovieShowingType.NOW_SHOWING.getUrlString());
        List<CineplexApiMovie> movieList = cineplexCrawlingApiService.getMovieNowshowing();
        for (CineplexApiMovie movie : movieList) {
            movieDAO.insertMovie(movie, MovieShowingType.NOW_SHOWING.getUrlString());
        }
        return true;
    }

    @Override
    @Transactional
    public boolean processMoviesCommingSoon() {
        movieDAO.deleteMovieList(MovieShowingType.COMING_SOON.getUrlString());
        List<CineplexApiMovie> movieList = cineplexCrawlingApiService.getMovieCommingsoon();
        for (CineplexApiMovie movie : movieList) {
            movieDAO.insertMovie(movie, MovieShowingType.COMING_SOON.getUrlString());
        }
        return true;
    }


    @Override
    public void moviePosterDownload() throws IOException {
        List<Map<String, String>> posterList = movieDAO.selectNewMoviePosterPathList(ApiDataProcessStatus.NEW.getStatus());
        // file path
        String today = LPDateUtil.getTodate(LPDateUtil.DATE_FORMAT_YMD);
        String filePath = cineplexFilePath + today + "/origin/";

        File dir = new File(filePath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (Map<String, String> poster : posterList) {
            String posterOrigin = cineplexUrl + poster.get("posterOrigin");
            String lpPosterOrigin = filePath + poster.get("fileName");
            String posterExtension = poster.get("extension");
            String movieId = poster.get("movieId");

            ImageUtil.lpMoviePosterOriginDownload(posterOrigin, lpPosterOrigin, posterExtension);
            movieDAO.updateLpPosterOriginPath(movieId, lpPosterOrigin);
        }
    }

  
}
