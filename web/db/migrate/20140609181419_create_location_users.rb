class CreateLocationUsers < ActiveRecord::Migration
	def change
		create_table :location_users do |t|
			t.integer :location_id
			t.integer :user_id

			t.timestamps
		end
		
		add_index :location_users, [:location_id, :user_id], unique: true
	end
end
