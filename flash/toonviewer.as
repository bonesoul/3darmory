package  {

	import org.papervision3d.materials.utils.MaterialsList;
	import org.papervision3d.objects.DisplayObject3D;
	import org.papervision3d.objects.parsers.Collada;
	import org.papervision3d.objects.parsers.Max3DS;
	
	import org.papervision3d.objects.parsers.Max3DS;
	
	import flash.filters.*;
	
	public class toonviewer extends pv3d_base 
	{
		public var cow:DisplayObject3D;
		public var distance:Number = 1000;
		

		
		//public var filter:GlowFilter = new GlowFilter (0x000000, 1, 5, 5, 50, 1, false, false); // cel-shading
		//public var filter:BevelFilter = new BevelFilter(); // bevel
		//public var filter:BlurFilter = new BlurFilter(); // blur
		
		
		
		protected var sceneWidth:Number;
		protected var sceneHeight:Number;
		
		public function toonviewer() {
			sceneWidth = stage.stageWidth;
			sceneHeight = stage.stageHeight;
			
			init(sceneWidth, sceneHeight);
			//viewport.filters = [filter];
		}
		override protected function init3d():void {
			
			// 3DS
			//var max3ds_cow:Max3DS = new Max3DS();
			//max3ds_cow.load("offhand_dalaran_d_01.mom");
			//default_scene.addChild(max3ds_cow);
			
			
			cow = new Collada("http://papervision2.com/wp-content/downloads/dae/cow.dae");
			
			
			cow.moveDown(250);
			cow.scale = 3;
			default_scene.addChild(cow);
		}
		override protected function processFrame():void {
			//cow.yaw(2);
			//default_camera.y = -(((mouseY - (sceneWidth / 2)) / sceneHeight) * 1600);
			//default_camera.moveForward(default_camera.distanceTo(cow) - distance);
			cow.rotationY = -((mouseX / sceneWidth) * 360) //Rotation

		}
	}
}


