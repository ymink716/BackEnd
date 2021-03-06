package kr.or.watermelon.ticket.reservation.service;

import kr.or.watermelon.ticket.reservation.domain.Ticket;
import kr.or.watermelon.ticket.reservation.dto.TicketDto;
import kr.or.watermelon.ticket.reservation.repository.ReservationRepository;
import kr.or.watermelon.ticket.reservation.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public List<TicketDto> cancel(Long reservationId) {
        List<Ticket> ticketList = ticketRepository.findByReservationId(reservationId);
        ticketList.forEach(ticket -> {
                    ticket.setSold(false);
                    ticket.setReservation(null);
        });
        List<Ticket> canceledTickets = ticketRepository.saveAll(ticketList);

        return canceledTickets
                .stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                .collect(Collectors.toList());
    }

    public List<TicketDto> getListByPerformance(Long performanceId) {
        List<Ticket> ticketList = ticketRepository.findByPerformanceId(performanceId);
        return ticketList
                .stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                .collect(Collectors.toList());
    }

    public List<TicketDto> getListByReservation(Long reservationId) {
        List<Ticket> ticketList = ticketRepository.findByReservationId(reservationId);
        return ticketList
                .stream()
                .map(ticket -> modelMapper.map(ticket, TicketDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long performanceId) {
        ticketRepository.deleteALlByPerformanceId(performanceId);
    }
}
