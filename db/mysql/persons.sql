

CREATE SCHEMA IF NOT EXISTS `fakepersonadb` DEFAULT CHARACTER SET utf8 ;
use "fakepersonadb";

CREATE TABLE persons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    gender VARCHAR(10)
);

INSERT INTO persons (firstName, lastName, gender) VALUES
('Annemette P.', 'Nilsson', 'female'),
('Freja O.', 'Thygesen', 'female'),
('Anna S.', 'Jespersen', 'female'),
('Rosa M.', 'Bang', 'female'),
('Nicolai T.', 'Bech', 'male'),
('Mimir S.', 'Krogh', 'male'),
('Mike M.', 'Eriksen', 'male'),
('Christian A.', 'Jacobsen', 'male'),
('Villads E.', 'Olesen', 'male');