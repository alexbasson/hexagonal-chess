require 'user_management/user_id'
require 'user_management/friendship'
require 'user_management/friendship_repository'
require 'user_management/list_friends'

RSpec.describe UserManagement::ListFriends do
  subject(:list_friends) { described_class.new(friendship_repository) }

  let(:friendship_repository) { instance_double(UserManagement::FriendshipRepository) }
  let(:owner_id)              { UserManagement::UserId.new(value: 'u1') }
  let(:friendship)            { UserManagement::Friendship.new(owner_id: owner_id, friend_id: UserManagement::UserId.new(value: 'u2')) }

  it 'returns friendships for the owner' do
    allow(friendship_repository).to receive(:find_by_owner_id).with(owner_id).and_return([friendship])

    expect(list_friends.call(owner_id)).to eq([friendship])
  end

  it 'returns empty when no friends' do
    allow(friendship_repository).to receive(:find_by_owner_id).with(owner_id).and_return([])

    expect(list_friends.call(owner_id)).to eq([])
  end
end
