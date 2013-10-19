package toonviewer;

class Armor implements Comparable {
	class ArmorGeoset {

		int index;
		int value;
		final Armor armor;

		public ArmorGeoset(Armor _armor) {
			this.armor = _armor;
			index = -1;
			value = 0;
		}
	}

	class ArmorModel {

		int race;
		int gender;
		int bone;
		Model model;

		public ArmorModel(Model m, int r, int g, int b) {
			race = r;
			gender = b;
			bone = b;
			model = m;
		}
	}

	class ArmorTexture {

		int slot;
		int gender;
		String name;
		Material mat;
		final Armor armor;

		public ArmorTexture(Armor _armor) {
			this.armor = _armor;
			slot = -1;
			gender = 0;
			name = null;
			mat = null;
		}
	}

	protected ArmorGeoset geo[];
	protected ArmorTexture tex[];
	protected ArmorModel mod[];
	protected int slot;
	protected int uniqueSlot;
	protected int geoA;
	protected int geoB;
	protected int geoC;
	protected String modelFile;
	protected Model model;

	public Armor(Model m, int s) {
		slot = 0;
		uniqueSlot = 0;
		geoA = 0;
		geoB = 0;
		geoC = 0;
		model = m;
		slot = s;
		uniqueSlot = Model.uniqueSlots[slot];
		modelFile = null;
		geo = null;
		tex = null;
		mod = null;
	}

	public Armor(Model m, int s, String modelStr, int race, int gender) {
		this(m, s);

		modelFile = modelStr;
		if (slot != 3)
			mod = new ArmorModel[1];
		else
			mod = new ArmorModel[2];
		for (int i = 0; i < mod.length; i++) {
			int bone = -1;
			if (model.mSlotBones != null && model.mSlotBones.length > 0) {
				int found = 0;
				for (int j = 0; j < model.mSlotBones.length; j++) {
					SlotBone sb = model.mSlotBones[j];
					if (sb.slot == slot && found == i) {
						bone = sb.bone;
						break;
					}
					if (sb.slot == slot)
						found++;
				}

			}
			mod[i] = new ArmorModel(new Model(), race, gender, bone);
			String fn = "models/" + modelFile;
			if (slot == 1)
				fn = fn + "_" + race + "_" + gender;
			else if (slot == 3)
				fn = fn + "_" + (i + 1);
			fn = fn + ".mum";
			FileLoader loader = new FileLoader(1004, mod[i].model, fn
					.toLowerCase());
			loader.start();
		}

	}

	public int compareTo(Object o) {
		Armor a = (Armor) o;
		if (Model.slotSort[slot] < Model.slotSort[a.slot])
			return -1;
		return Model.slotSort[slot] != Model.slotSort[a.slot] ? 1 : 0;
	}
}
