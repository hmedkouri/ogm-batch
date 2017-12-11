package services;

import entity.IRS;
import entity.Trade;
import ids.ClientId;
import ids.PortfolioId;
import ids.TradeId;

import java.util.List;

public interface TradeService<T extends Trade> extends Service<T, TradeId> {

    Iterable<T> findBilateralTradesByClientId(ClientId clientId);

    <T extends Trade> Iterable<T> findByPortfolioId(PortfolioId... ids);

    Iterable<IRS> findAllIRS();

    <S extends T> Iterable<S> createOrUpdate(Iterable<S> trades);

    Iterable<T> findAllTradeByIds(List<TradeId> ids);
}