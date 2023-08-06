package com.mohistmc.netty;

import com.mohistmc.api.ServerAPI;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.DecoderException;
import net.minecraft.network.PacketBuffer;

import java.util.List;
import java.util.zip.Inflater;

public class NettyPacketDecompressor extends ByteToMessageDecoder {
    private final Inflater inflater;
    private int threshold;

    public NettyPacketDecompressor(int p_i46006_1_) {
        this.threshold = p_i46006_1_;
        this.inflater = new Inflater();
    }

    protected void decode(ChannelHandlerContext p_decode_1_, ByteBuf p_decode_2_, List<Object> p_decode_3_) throws Exception {
        if (p_decode_2_.readableBytes() != 0) {
            PacketBuffer packetbuffer = new PacketBuffer(p_decode_2_);
            int i = packetbuffer.readVarInt();
            if (i == 0) {
                p_decode_3_.add(packetbuffer.readBytes(packetbuffer.readableBytes()));
            } else {
                if (i < this.threshold) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is below server threshold of " + this.threshold);
                }

                int packetsizelimits = ServerAPI.hasMod("randompatches") ? 2097152 : 8388608;

                if (i > packetsizelimits) {
                    throw new DecoderException("Badly compressed packet - size of " + i + " is larger than protocol maximum of " + packetsizelimits);
                }

                byte[] abyte = new byte[packetbuffer.readableBytes()];
                packetbuffer.readBytes(abyte);
                this.inflater.setInput(abyte);
                byte[] abyte1 = new byte[i];
                this.inflater.inflate(abyte1);
                p_decode_3_.add(Unpooled.wrappedBuffer(abyte1));
                this.inflater.reset();
            }

        }
    }

    public void setThreshold(int p_179303_1_) {
        this.threshold = p_179303_1_;
    }
}
