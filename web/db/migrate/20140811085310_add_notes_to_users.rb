class AddNotesToUsers < ActiveRecord::Migration
	def change
		add_column :users, :notes, :string
		add_index :users, [:name]
		add_index :users, [:surname]
	end
end
