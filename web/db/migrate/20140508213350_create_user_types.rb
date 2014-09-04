class CreateUserTypes < ActiveRecord::Migration
	def change
		create_table :user_types, id: false do |t|
			t.integer :user_id
			t.string :user_type

			t.timestamps
		end
	 
		add_index :user_types, [:user_id, :user_type], unique: true
	end
end
