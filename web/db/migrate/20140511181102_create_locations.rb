class CreateLocations < ActiveRecord::Migration
	def change
		create_table :locations do |t|
			t.string :name
			t.string :description
			t.string :address
			t.string :phone
			t.float :latitude
			t.float :longitude
			t.string :location_type
			t.boolean :active

			t.timestamps
		end
		
		add_index :locations, [:latitude, :longitude], unique: true
		add_index :locations, [:name]
	end
end
