package me.line.pay.th.api.inbound.cineplex.service;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nts.partner.payment.core.util.LPDateUtil;

import me.line.pay.th.api.inbound.cineplex.util.ImageUtil;
import me.line.pay.th.api.inbound.enums.ApiDataProcessStatus;
import me.line.pay.th.cineplex.api.enums.MovieShowingType;
import me.line.pay.th.cineplex.api.exceptions.CineplexApiException;
import me.line.pay.th.cineplex.api.model.CineplexApiCinema;
import me.line.pay.th.cineplex.api.model.CineplexApiMovie;
import me.line.pay.th.cineplex.api.model.CineplexApiMovieShowtime;
import me.line.pay.th.cineplex.api.model.CineplexApiPrintStream;
import me.line.pay.th.cineplex.api.service.CineplexCrawlingApiService;
import me.line.pay.th.cineplex.api.service.CineplexDataApiService;
import me.line.pay.th.cineplex.api.wsdl.dataservice.GetCinemaListAllRequest;
import me.line.pay.th.cineplex.api.wsdl.dataservice.GetMovieShowtimesRequest;
import me.line.pay.th.cineplex.api.wsdl.dataservice.GetPrintStreamRequest;
import me.line.pay.th.cineplex.dao.CinemaDAO;
import me.line.pay.th.cineplex.dao.CineplexMyTicketDAO;
import me.line.pay.th.cineplex.dao.CineplexPaymentDAO;
import me.line.pay.th.cineplex.dao.MovieDAO;
import me.line.pay.th.cineplex.dao.MovieShowtimesDAO;
import me.line.pay.th.cineplex.enums.CineplexPaymentWaitType;
import me.line.pay.th.cineplex.model.Cinema;
import me.line.pay.th.cineplex.model.MovieShowtimes;
import me.line.pay.th.cineplex.model.MyTicket;
import me.line.pay.th.cineplex.model.payment.CineplexPayment;
import me.line.pay.th.cineplex.model.payment.CineplexPaymentWait;
import me.line.pay.th.cineplex.service.CineplexMyTicketService;
import me.line.pay.th.cineplex.service.CineplexPaymentService;
import me.line.pay.th.cineplex.service.CineplexPaymentWaitService;
import me.line.pay.th.cineplex.service.CineplexShareService;
import me.line.pay.th.common.enums.LangLocaleType;
import me.line.pay.th.common.util.DateUtil;
import me.line.pay.th.util.date.DateFormat;

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
