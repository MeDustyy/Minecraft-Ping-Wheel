package nx.pingwheel.client.helper;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vector4f;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@Getter
public class PingData {

	@Setter
	private Vec3d pos;
	@Nullable
	private final UUID uuid;
	private final int spawnTime;

	public int aliveTime;
	@Nullable
	public Vector4f screenPos;
	@Nullable
	public ItemStack itemStack;

	public PingData(Vec3d pos, @Nullable UUID uuid, int spawnTime) {
		this.pos = pos;
		this.uuid = uuid;
		this.spawnTime = spawnTime;
	}
}
