drop table prefloptable;
create table preFlopTable(
            cardType varchar(32) NOT NULL,
            card1Value varchar(32)NOT NULL ,
            card2Value varchar(32) NOT NULL ,
            nPlayers varchar(32) NOT NULL,
            winRate varchar(32)NOT NULL,
	    tieRate varchar(32)NOT NULL,
	    lossRate  varchar(32)NOT NULL,
            PRIMARY KEY(cardType,card1Value,card2Value,nPlayers,lossRate,tieRate,winRate)

);