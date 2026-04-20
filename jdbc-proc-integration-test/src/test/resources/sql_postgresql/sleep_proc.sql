CREATE OR REPLACE FUNCTION sleep_proc(i_seconds int) RETURNS void AS $$
BEGIN
  PERFORM pg_sleep(i_seconds);
END;
$$ LANGUAGE plpgsql;
