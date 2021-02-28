package me.rubataga.everyhunt.utils;

import me.rubataga.everyhunt.roles.Hunter;
import me.rubataga.everyhunt.services.TargetManager;
import org.bukkit.Bukkit;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;

import java.nio.ByteBuffer;
import java.util.UUID;

public class TrackingCompassTagType implements PersistentDataType<byte[], Hunter> {

    @Override
    public Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public Class<Hunter> getComplexType() {
        return Hunter.class;
    }

    @Override
    public byte[] toPrimitive(Hunter complex, PersistentDataAdapterContext context) {
        //return complex.getEntity().getUniqueId();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        UUID id = complex.getEntity().getUniqueId();
        bb.putLong(id.getMostSignificantBits());
        bb.putLong(id.getLeastSignificantBits());
        return bb.array();
    }

    @Override
    public Hunter fromPrimitive(byte[] primitive, PersistentDataAdapterContext context) {
        ByteBuffer bb = ByteBuffer.wrap(primitive);
        long mostSigBits = bb.getLong();
        long leastSigBits = bb.getLong();
        UUID id = new UUID(mostSigBits,leastSigBits);
        return TargetManager.getHunter(Bukkit.getPlayer(id));
        //return TargetManager.getHunter(Bukkit.getPlayer(primitive));
    }
}
