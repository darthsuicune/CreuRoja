class AddIndexToUsersEmail < ActiveRecord::Migration
	def change
		add_column :users, :active, :boolean, default: true
		add_index :users, :email, unique: true
	end
end
