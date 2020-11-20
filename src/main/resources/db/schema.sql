CREATE TABLE IF NOT EXISTS message (
    id bigint AUTO_INCREMENT,
    text VARCHAR(128) NOT NULL,
    primary key (id)
);

CREATE TABLE IF NOT EXISTS headers (
    id INT NOT NULL AUTO_INCREMENT,
    head VARCHAR(256),
    FOREIGN KEY (id) references message(id),
    primary key (id)
);