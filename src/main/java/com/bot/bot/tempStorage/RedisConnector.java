package com.bot.bot.tempStorage;

import com.bot.bot.TempStorage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Getter
@Setter
@Component
@Slf4j
public class RedisConnector implements TempStorage {
    Jedis jedis;
    private final String SEP = ";";

    public RedisConnector() {
        this.jedis = new Jedis("127.0.0.1", 6379);
        log.info("Redis connection was established.");
    }

    @Override
    public String get(String key) {
        String result = jedis.get(key);
        log.info("The following value is obtained by key " + key + ": " + result);
        return result;
    }

    @Override
    public void set(String key, String data) {
        log.info("Data " + data + "will be set to redis with key " + key);
        jedis.set(key, data);
    }

    //Не используется, может пригодится при работе с листами
//    public List<String> getList(String key) {
//        List<String> res = new ArrayList<>();
//        long size = jedis.llen(key.getBytes());
//        for (long i = 0; i <= size; i++) {
//            byte[] b = jedis.rpop(key.getBytes());
//            if (b != null) {
//                res.add(new String(b));
//            }
//        }
//        return res;
//    }
//
//    public void setList(String key, List<String> data) {
//        String[] d = data.toArray(new String[0]);
//        jedis.lpush(key, d);
//    }
}
