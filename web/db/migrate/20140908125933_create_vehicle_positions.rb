class CreateVehiclePositions < ActiveRecord::Migration
	def change
		create_table :vehicle_positions do |t|
			t.integer :vehicle_id
			t.string :indicative
			t.float :latitude, limit: 50
			t.float :longitude, limit: 50

			t.timestamps
		end
		
		add_index :vehicle_positions, [:vehicle_id]
	end
end
