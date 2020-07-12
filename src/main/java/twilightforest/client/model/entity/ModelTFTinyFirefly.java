package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import twilightforest.entity.passive.EntityTFMobileFirefly;

public class ModelTFTinyFirefly<T extends EntityTFMobileFirefly> extends SegmentedModel<T> {

	public ModelTFTinyFirefly() {
		glow1 = new ModelRenderer(this, 20, 0);
		glow1.addBox(-5F, -5F, 0F, 10, 10, 0, 0F);
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(glow1);
	}

	@Override
	public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	//fields
	public ModelRenderer glow1;
}
