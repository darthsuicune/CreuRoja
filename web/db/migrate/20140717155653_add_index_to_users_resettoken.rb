class AddIndexToUsersResettoken < ActiveRecord::Migration
  def change
	  add_index :users, [:resettoken], unique: true
  end
end
