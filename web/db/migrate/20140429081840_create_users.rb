class CreateUsers < ActiveRecord::Migration
	def change
		create_table :users do |t|
			t.string :name
			t.string :surname
			t.string :email
			t.string :password_digest
			t.string :resettoken
			t.integer :resettime
			t.string :language
			t.string :role

			t.timestamps
		end
	end
end
