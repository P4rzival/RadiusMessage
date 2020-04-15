//I made this, but it has not been tested

CREATE TABLE post_archive

/*user_message_text
radius
location_x
location_y
message_duration
*/

ALTER TABLE post_archive
ADD user_message_text LONGTEXT;

ALTER TABLE post_archive
ADD radius DOUBLE(2,1);

ALTER TABLE post_archive
ADD location_x DOUBLE(2,1);

ALTER TABLE post_archive
ADD location_y DOUBLE(2,1);

ALTER TABLE post_archive
ADD message_duration INT(255);
