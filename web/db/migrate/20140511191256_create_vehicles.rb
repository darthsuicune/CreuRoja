class CreateVehicles < ActiveRecord::Migration
	def change
		create_table :vehicles do |t|
			t.string :brand
			t.string :model
			t.string :license
			t.string :indicative
			t.string :vehicle_type
			t.integer :places
			t.string :notes
			t.boolean :operative

			t.timestamps
		end
		
		add_index :vehicles, [:license]
		add_index :vehicles, [:indicative]
		add_index :vehicles, [:vehicle_type]
	end
end
